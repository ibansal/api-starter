package com.companyname.projectname.common;

import javax.servlet.http.HttpServletRequest;

public enum RoleType {
    ADMIN("ADMIN") {
        @Override
        public boolean isAllowed(HttpServletRequest httpServletRequest) {
            return true;
        }
    },
    FINANCE("FINANCE") {
        @Override
        public boolean isAllowed(HttpServletRequest httpServletRequest) {
            return true;
        }
    },
    SELLER("SELLER") {
        @Override
        public boolean isAllowed(HttpServletRequest httpServletRequest) {
            return true;
        }
    };

    private final String label;

    RoleType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return this.label;
    }

    public static RoleType getRole(String label) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getLabel().equalsIgnoreCase(label)) {
                return roleType;
            }
        }
        return null;
    }

    abstract public boolean isAllowed(HttpServletRequest httpServletRequest);
}