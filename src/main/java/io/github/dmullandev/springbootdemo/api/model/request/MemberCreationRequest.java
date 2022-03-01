package io.github.dmullandev.springbootdemo.api.model.request;

import lombok.Data;

@Data
public class MemberCreationRequest {
	private String firstName;
	private String lastName;
}
