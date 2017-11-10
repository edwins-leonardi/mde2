package com.contaazul.invoiceissuer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MailConf {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String bcc;
	private String sender;
	private String subject;
	private String cancelSubject;
	private String cceSubject;
}
