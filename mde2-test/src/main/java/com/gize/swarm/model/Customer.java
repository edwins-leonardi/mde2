package com.gize.swarm.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Customer {
	@Id
	private Long id;
	private String name;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
	private List<Issue> issues;

	public Customer() {
	}

	public Customer(final Long id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(final List<Issue> issues) {
		this.issues = issues;
	}
}
