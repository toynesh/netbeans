package com.pdslkenya.airtelrx.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Snd.class)
public abstract class Snd_ {

	public static volatile SingularAttribute<Snd, Date> sndBuffered;
	public static volatile SingularAttribute<Snd, Date> sndSentat;
	public static volatile SingularAttribute<Snd, Long> sndId;
	public static volatile SingularAttribute<Snd, String> sndTo;
	public static volatile SingularAttribute<Snd, String> sndSender;
	public static volatile SingularAttribute<Snd, Date> sndFailure;
	public static volatile SingularAttribute<Snd, Date> sndSubmitted;
	public static volatile SingularAttribute<Snd, Date> sndSuccess;
	public static volatile SingularAttribute<Snd, String> sndmessageId;
	public static volatile SingularAttribute<Snd, String> sndSmsc;
	public static volatile SingularAttribute<Snd, Integer> sndLast;
	public static volatile SingularAttribute<Snd, Date> sndIntermediate;
	public static volatile SingularAttribute<Snd, Date> sndRejected;
	public static volatile SingularAttribute<Snd, String> sndTxt;
	public static volatile SingularAttribute<Snd, Integer> status;

}

