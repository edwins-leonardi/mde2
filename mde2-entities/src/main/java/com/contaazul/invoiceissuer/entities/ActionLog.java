package com.contaazul.invoiceissuer.entities;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "log")
@SequenceGenerator(name = "seq_log", sequenceName = "seq_log", allocationSize = 1)
public class ActionLog {
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_log")
	private Long id;
	private Long invoiceId;
	private Long tenantId;
	@Enumerated(STRING)
	private ActionLogType type;
	@Enumerated(STRING)
	private ActionLogMessage message;
	private String text;
	private String key;
	private String uf;
	@Temporal(TIMESTAMP)
	private Date createdAt;
	@Lob
	private String data;
	@Enumerated(STRING)
	private App app;
	private String email;

	@PrePersist
	public void prePersist() {
		this.createdAt = new Date();
	}

	public ActionLog(Long invoiceId, Long tenantId, String key) {
		this.invoiceId = invoiceId;
		this.tenantId = tenantId;
		this.key = key;
	}

	public ActionLog(Long id, Long invoiceId, Long tenantId, ActionLogType type, ActionLogMessage message, String text,
			String key, Date createdAt, String data, App app, String email) {
		super();
		this.id = id;
		this.invoiceId = invoiceId;
		this.tenantId = tenantId;
		this.type = type;
		this.message = message;
		this.text = text;
		this.key = key;
		this.createdAt = createdAt;
		this.data = data;
		this.app = app;
		this.email = email;
	}
}
