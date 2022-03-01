package io.github.dmullandev.springbootdemo.api.model.request;

import java.util.List;

import lombok.Data;

@Data
public class BookLendRequest {
	private List<String> bookIds;
	private String memberId;
}
