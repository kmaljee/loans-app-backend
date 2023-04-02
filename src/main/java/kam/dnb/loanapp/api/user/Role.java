package kam.dnb.loanapp.api.user;

public enum Role {
    CUSTOMER,
    ADVISER;

    public static final String AUTHORITY_ROLE_PREFIX = "ROLE_";

    /**
     * @return Whether the specified authority string matches this Role
     */
    public boolean matchesAuthority(final String authority) {
        return (AUTHORITY_ROLE_PREFIX + name()).equals(authority);
    }
}
