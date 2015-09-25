package com.companyname.projectname.test.config;

import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.regex.Pattern;

class WebPackageFilter extends RegexPatternTypeFilter {
    public WebPackageFilter() {
        super(Pattern.compile("com\\.companyname\\.projectname\\.config\\..*"));
    }
}
