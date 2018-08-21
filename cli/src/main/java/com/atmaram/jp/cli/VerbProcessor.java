package com.atmaram.jp.cli;

import com.atmaram.jp.model.Unit;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;

public class VerbProcessor<T extends Unit> {
    String verb;
    Transformer<T> transformer;

    @FunctionalInterface
    public interface Transformer<E extends Unit>{
        public E transform(File file) throws FileNotFoundException, ParseException;
    }

    public VerbProcessor(String verb, Transformer<T> transformer) {
        this.verb = verb;
        this.transformer = transformer;
    }
}
