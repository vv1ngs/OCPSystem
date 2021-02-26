package org.vvings.ocpsystem.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.vvings.ocpsystem.util.MD5Util;

public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return MD5Util.MD5EncodeUtf8(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(MD5Util.MD5EncodeUtf8(charSequence.toString()));
    }
}
