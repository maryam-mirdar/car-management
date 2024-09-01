package org.mirdar.api.model.dto.filter;

import com.mwga.storage.orm.model.Filterable;
import com.mwga.storage.orm.model.SortCriteria;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class CarFilter implements Filterable {
    private String id;
    private String model;
    private String licensePlate;
    private String personId;
    private String personFirstName;
    private String personLastName;
    private boolean distinct;
    private List<SortCriteria> sortList;
}
