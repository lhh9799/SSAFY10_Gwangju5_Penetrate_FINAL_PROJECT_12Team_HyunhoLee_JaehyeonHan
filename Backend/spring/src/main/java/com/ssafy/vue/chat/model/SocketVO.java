package com.ssafy.vue.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketVO {
	private String userName;
	private String content;
}
