package com.neppiness.recruitment.domain;

public enum Authority {

    MEMBER,
    MANAGER,
    ADMIN;

    public static Authority parseAuthority(String value) {
        for (Authority authority : Authority.values()) {
            if (authority.toString().equalsIgnoreCase(value)) {
                return authority;
            }
        }
        return null;
    }

}
