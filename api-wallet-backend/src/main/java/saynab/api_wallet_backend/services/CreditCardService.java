package saynab.api_wallet_backend.services;

import jakarta.transaction.Transactional;
import org.apache.catalina.connector.InputBuffer;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import saynab.api_wallet_backend.controllers.DTOs.CreditCardResponseDTO;
import saynab.api_wallet_backend.controllers.DTOs.RequestSingleCardDTO;
import saynab.api_wallet_backend.entities.CreditCard;
import saynab.api_wallet_backend.entities.User;
import saynab.api_wallet_backend.repositories.CreditCardRepository;
import saynab.api_wallet_backend.repositories.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CreditCardService {

    private CreditCardRepository creditCardRepository;

    private UserRepository userRepository;

    private static final int BATCH_SIZE = 1000;

    public CreditCardService(CreditCardRepository creditCardRepository, UserRepository userRepository) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
    }

    public Void addSingleCard(RequestSingleCardDTO dto,
                              JwtAuthenticationToken token){

        var user = userRepository.findById(UUID.fromString(token.getName()));

        var card = new CreditCard();
        card.setRawCardNumber(dto.cardNumber());
        card.setUser(user.get());
        card.setLoteSequence("XXXXXX");
        card.setRowFileIdentifier("0");
        creditCardRepository.save(card);

        return null;
    }

    public CreditCardResponseDTO findCardById (String rawCardNumber,
                              JwtAuthenticationToken token){

        List<CreditCard> list = creditCardRepository.findByUserId(UUID.fromString(token.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        System.out.println("deu certo a consulta  -> " + list.getLast());
        Optional<CreditCard> cardFound = list.stream()
                .filter(
                card -> card.getRawCardNumber().equals(rawCardNumber))
                .findFirst();

        return cardFound.map(CreditCardResponseDTO::fromCreditCard)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Card not found"));

    }

     public Void extractCardsFromTextFile (MultipartFile file,
                                                      JwtAuthenticationToken token) throws IOException {

         List<CreditCard> cards = parseFile(file.getInputStream());

         var user = userRepository.findById(UUID.fromString(token.getName()));

        for(CreditCard card: cards){
            card.setUser(user.get());
            creditCardRepository.save(card);
        }
        return null;

    }

    private List<CreditCard> parseFile(InputStream input) {
        List<CreditCard> cards = new ArrayList<>();
        List<String> rows = IOUtils.readLines(input,StandardCharsets.UTF_8);

        String lote =null;
        int totalRecordsInFile = 0;

        for (String row: rows) {

            if (row.length() < 51) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {

                if (row.startsWith("DESAFIO-HYPERATIVA")) {
                    lote = row.substring(37, 45).trim();
                    totalRecordsInFile = Integer.parseInt(row.substring(45, 51).trim());
                } else if (row.matches("C\\d+\\s+.*")) {
                    String rowIdentifier = row.substring(0, 1).trim();
                    System.out.println("O valor do rowIdentifier é " + rowIdentifier);
                    String loteSequence = row.substring(1, 7).trim();
                    System.out.println("O valor do loteSequence é " + loteSequence);
                    String cardNumber = row.substring(7, 26).trim();
                    System.out.println("O valor do cardNumber é " + cardNumber);

                    CreditCard card = new CreditCard();
                    card.setRawCardNumber(cardNumber);
                    card.setLoteSequence(loteSequence);
                    card.setRowFileIdentifier(rowIdentifier);
                    cards.add(card);
                } else if (row.matches("LOTE.*")) {
                    String trailerLote = row.substring(0, 8).trim();
                    int trailerQtd = Integer.parseInt(row.substring(8, 14).trim());

                    if (!trailerLote.equals(lote) || trailerQtd != cards.size()) {
                        throw new RuntimeException("Trailer invalid! Lote: " + trailerLote +
                                ", Esperado " + lote);
                    }
                }

            }
        }

        return cards;
    }


    public void processFile(MultipartFile file,
                            JwtAuthenticationToken token) {

        User user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow();

        List<CreditCard> batch = new ArrayList<>(BATCH_SIZE);

        try( BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream()))) {

            String line;
            long lineNumber = 0;

            while((line= reader.readLine()) != null){
                lineNumber++;

                if (isLineValid(line)) {

                    CreditCard card = parseFile(line, user);
                    batch.add(card);

                    if (batch.size() == BATCH_SIZE) {
                        saveBatch(batch);
                        batch.clear();
                    }
                }

            }

            if(!batch.isEmpty()){
                saveBatch(batch);
            }


        } catch (Exception e) {
            throw new RuntimeException("Error by processing the file");
        }

    }

    @Transactional
    protected void saveBatch(List<CreditCard> batch){
        creditCardRepository.saveAll(batch);
        creditCardRepository.flush();

    }

    private CreditCard parseFile(String line,User user){

        CreditCard card = new CreditCard();

        card.setRowFileIdentifier(line.substring(0, 1).trim());
        card.setLoteSequence(line.substring(1, 7).trim());
        card.setRawCardNumber(line.substring(7, 26).trim());
        card.setUser(user);
        return card;

    }

    private boolean isLineValid(String line){
        return line.matches("C\\d+\\s+.*") && line.length()>=51;
    }


}

