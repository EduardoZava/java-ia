package com.eduardozava.javaia.skills;

public interface Skill<I, O> {
    O execute(I input);
}
