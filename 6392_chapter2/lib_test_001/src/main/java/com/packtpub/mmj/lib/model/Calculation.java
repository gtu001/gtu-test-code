package com.packtpub.mmj.lib.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Calculation {

    String function;
    private List<String> input;
    private List<String> output;

    public Calculation() {
    }

    public Calculation(List<String> input, List<String> output, String function) {
        this.function = function;
        this.input = input;
        this.output = output;
    }
}