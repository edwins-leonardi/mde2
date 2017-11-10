package com.contaazul.invoiceissuer.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class JobConf {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String hostname;
	private Boolean enabled;

	public JobConf(String hostname, boolean enabled) {
		super();
		this.hostname = hostname;
		this.enabled = enabled;
	}
}
