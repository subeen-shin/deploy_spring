package kr.fast.community.dto;

/** success : 요청 성공 여부
 *  message : 보낼 안내 문구
 * 
 * */
public record MessageResponse(boolean success, String message) {}
