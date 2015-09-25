package com.companyname.projectname.repository;

import com.companyname.projectname.dto.AccountInfoDto;
import com.companyname.projectname.dto.Direction;
import com.companyname.projectname.dto.Op;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Map;

public interface AccountRepositoryCustom{
    List<AccountInfoDto> findAllAccountInfo(List<Triple<String, Op, Object>> filters, int pageNumber, int pageSize, List<Pair<String, Direction>> sortBy);
    int findAllAccountInfoCount(Map<String, String> filters, String sortBy);

    int findAllAccountInfoCount(List<Triple<String,Op,Object>> filtersList);
}