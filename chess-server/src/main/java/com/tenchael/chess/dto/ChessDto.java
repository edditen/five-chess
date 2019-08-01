package com.tenchael.chess.dto;

import java.util.HashMap;
import java.util.Map;

public class ChessDto {
    private ChessHeader header = new ChessHeader();
    private Map<String, Object> body = new HashMap<>();

    public ChessDto() {
    }

    public ChessDto(ChessHeader header, Map<String, Object> body) {
        this.header = header;
        this.body = body;
    }

    public ChessHeader getHeader() {
        return header;
    }

    public void setHeader(ChessHeader header) {
        this.header = header;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
