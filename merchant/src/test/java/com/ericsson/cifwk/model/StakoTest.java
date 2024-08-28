package com.ericsson.cifwk.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StakoTest {

    @Test
    public void fromESW() throws Exception {
        assertThat(Stako.fromESW("ESW1"), is(Stako.PREFERRED));
        assertThat(Stako.fromESW("ESW2"), is(Stako.GENERAL_USE));
        assertThat(Stako.fromESW("ESW3"), is(Stako.RESTRICTED_USE));
        assertThat(Stako.fromESW("ESW4"), is(Stako.BANNED));
    }
}