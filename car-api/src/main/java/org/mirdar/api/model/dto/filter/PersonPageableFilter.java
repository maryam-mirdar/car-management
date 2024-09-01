package org.mirdar.api.model.dto.filter;

import com.mwga.storage.orm.model.PageableFilterable;
import lombok.Getter;
import lombok.Setter;
import org.mirdar.api.model.dto.filter.PersonFilter;

@Getter
@Setter
public class PersonPageableFilter extends PersonFilter implements PageableFilterable {
    private Integer page;
    private Integer size;

    public PersonPageableFilter() {
        this.page = 1;
        this.size = 15;
    }
}