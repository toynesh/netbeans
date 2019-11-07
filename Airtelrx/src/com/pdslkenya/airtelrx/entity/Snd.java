/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdslkenya.airtelrx.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tesla
 */
@Entity
@Table(name = "snd")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Snd.findAll", query = "SELECT s FROM Snd s"),
    @NamedQuery(name = "Snd.findBySndId", query = "SELECT s FROM Snd s WHERE s.sndId = :sndId"),
    @NamedQuery(name = "Snd.findBySndSender", query = "SELECT s FROM Snd s WHERE s.sndSender = :sndSender"),
    @NamedQuery(name = "Snd.findBySndTo", query = "SELECT s FROM Snd s WHERE s.sndTo = :sndTo"),
    @NamedQuery(name = "Snd.findBySndSmsc", query = "SELECT s FROM Snd s WHERE s.sndSmsc = :sndSmsc"),
    @NamedQuery(name = "Snd.findBySndSentat", query = "SELECT s FROM Snd s WHERE s.sndSentat = :sndSentat"),
    @NamedQuery(name = "Snd.findBySndSuccess", query = "SELECT s FROM Snd s WHERE s.sndSuccess = :sndSuccess"),
    @NamedQuery(name = "Snd.findBySndFailure", query = "SELECT s FROM Snd s WHERE s.sndFailure = :sndFailure"),
    @NamedQuery(name = "Snd.findBySndSubmitted", query = "SELECT s FROM Snd s WHERE s.sndSubmitted = :sndSubmitted"),
    @NamedQuery(name = "Snd.findBySndBuffered", query = "SELECT s FROM Snd s WHERE s.sndBuffered = :sndBuffered"),
    @NamedQuery(name = "Snd.findBySndRejected", query = "SELECT s FROM Snd s WHERE s.sndRejected = :sndRejected"),
    @NamedQuery(name = "Snd.findBySndIntermediate", query = "SELECT s FROM Snd s WHERE s.sndIntermediate = :sndIntermediate"),
    @NamedQuery(name = "Snd.findBySndLast", query = "SELECT s FROM Snd s WHERE s.sndLast = :sndLast"),
    @NamedQuery(name = "Snd.findByStatus", query = "SELECT s FROM Snd s WHERE s.status = :status"),
    @NamedQuery(name = "Snd.findBySndmessageId", query = "SELECT s FROM Snd s WHERE s.sndmessageId = :sndmessageId")})
public class Snd implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "snd_id")
    private Long sndId;
    @Column(name = "snd_sender")
    private String sndSender;
    @Column(name = "snd_to")
    private String sndTo;
    @Lob
    @Column(name = "snd_txt")
    private String sndTxt;
    @Column(name = "snd_smsc")
    private String sndSmsc;
    @Basic(optional = false)
    @Column(name = "snd_sentat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sndSentat;
    @Column(name = "snd_success")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sndSuccess;
    @Column(name = "snd_failure")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sndFailure;
    @Column(name = "snd_submitted")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sndSubmitted;
    @Column(name = "snd_buffered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sndBuffered;
    @Column(name = "snd_rejected")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sndRejected;
    @Column(name = "snd_intermediate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sndIntermediate;
    @Basic(optional = false)
    @Column(name = "snd_last")
    private int sndLast;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @Column(name = "snd_messageId")
    private String sndmessageId;

    public Snd() {
    }

    public Snd(Long sndId) {
        this.sndId = sndId;
    }

    public Snd(Long sndId, Date sndSentat, int sndLast, int status) {
        this.sndId = sndId;
        this.sndSentat = sndSentat;
        this.sndLast = sndLast;
        this.status = status;
    }

    public Long getSndId() {
        return sndId;
    }

    public void setSndId(Long sndId) {
        this.sndId = sndId;
    }

    public String getSndSender() {
        return sndSender;
    }

    public void setSndSender(String sndSender) {
        this.sndSender = sndSender;
    }

    public String getSndTo() {
        return sndTo;
    }

    public void setSndTo(String sndTo) {
        this.sndTo = sndTo;
    }

    public String getSndTxt() {
        return sndTxt;
    }

    public void setSndTxt(String sndTxt) {
        this.sndTxt = sndTxt;
    }

    public String getSndSmsc() {
        return sndSmsc;
    }

    public void setSndSmsc(String sndSmsc) {
        this.sndSmsc = sndSmsc;
    }

    public Date getSndSentat() {
        return sndSentat;
    }

    public void setSndSentat(Date sndSentat) {
        this.sndSentat = sndSentat;
    }

    public Date getSndSuccess() {
        return sndSuccess;
    }

    public void setSndSuccess(Date sndSuccess) {
        this.sndSuccess = sndSuccess;
    }

    public Date getSndFailure() {
        return sndFailure;
    }

    public void setSndFailure(Date sndFailure) {
        this.sndFailure = sndFailure;
    }

    public Date getSndSubmitted() {
        return sndSubmitted;
    }

    public void setSndSubmitted(Date sndSubmitted) {
        this.sndSubmitted = sndSubmitted;
    }

    public Date getSndBuffered() {
        return sndBuffered;
    }

    public void setSndBuffered(Date sndBuffered) {
        this.sndBuffered = sndBuffered;
    }

    public Date getSndRejected() {
        return sndRejected;
    }

    public void setSndRejected(Date sndRejected) {
        this.sndRejected = sndRejected;
    }

    public Date getSndIntermediate() {
        return sndIntermediate;
    }

    public void setSndIntermediate(Date sndIntermediate) {
        this.sndIntermediate = sndIntermediate;
    }

    public int getSndLast() {
        return sndLast;
    }

    public void setSndLast(int sndLast) {
        this.sndLast = sndLast;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSndmessageId() {
        return sndmessageId;
    }

    public void setSndmessageId(String sndmessageId) {
        this.sndmessageId = sndmessageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sndId != null ? sndId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Snd)) {
            return false;
        }
        Snd other = (Snd) object;
        if ((this.sndId == null && other.sndId != null) || (this.sndId != null && !this.sndId.equals(other.sndId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pdslkenya.airtelrx.entity.Snd[ sndId=" + sndId + " ]";
    }
    
}
