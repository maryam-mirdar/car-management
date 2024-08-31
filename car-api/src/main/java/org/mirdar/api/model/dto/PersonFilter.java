package org.mirdar.api.model.dto;

import com.mwga.storage.orm.model.Filterable;
import com.mwga.storage.orm.model.SortCriteria;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonFilter implements Filterable {
    private String id;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private boolean distinct;
    private List<SortCriteria> sortList;
}
