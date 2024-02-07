package com.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
// import static org.junit.jupiter.api.Assertions.*;

@Getter
@Setter
@ToString
public class LomBokTest {

    private String str;
    private int num;

    @Test
    public void LombokTest() {
        LomBokTest lomBokTest = new LomBokTest();
        lomBokTest.setStr("TEST");
        lomBokTest.setNum(1);

        assertThat(lomBokTest.getStr()).isEqualTo("TEST");
        assertThat(lomBokTest.getNum()).isEqualTo(1);
    }

}
