package org.mirdar.api.model.dto;

import com.mwga.storage.orm.model.Filterable;
import com.mwga.storage.orm.model.SortCriteria;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarFilter implements Filterable {
    private String id;
    private String model;
    private String licensePlate;
    private Long personId;
    private boolean distinct;
    private List<SortCriteria> sortList;
}
