package saynab.api_wallet_backend.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import saynab.api_wallet_backend.services.CryptoService;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="creditCards")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "row_file_identifier")
    private String rowFileIdentifier;

    @Column(name = "lote_sequence")
    private String loteSequence;


    @CreationTimestamp
    private Instant creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "card_number")
    private String encryptedCardNumber;

    @Transient
    private String rawCardNumber;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRowFileIdentifier() {
        return rowFileIdentifier;
    }

    public void setRowFileIdentifier(String rowFileIdentifier) {
        this.rowFileIdentifier = rowFileIdentifier;
    }

    public String getLoteSequence() {
        return loteSequence;
    }

    public void setLoteSequence(String loteSequence) {
        this.loteSequence = loteSequence;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public void setEncryptedCardNumber(String encryptedCardNumber) {
        this.encryptedCardNumber = encryptedCardNumber;
    }

    public String getRawCardNumber() {
        return rawCardNumber;
    }

    public void setRawCardNumber(String rawCardNumber) {
        this.rawCardNumber = rawCardNumber;
    }

    @PrePersist
    public void prePersist(){
        this.encryptedCardNumber = CryptoService.encrypt(rawCardNumber);
    }

    @PostLoad
    public void postLoad(){
        this.rawCardNumber = CryptoService.decrypt(encryptedCardNumber);
    }
}