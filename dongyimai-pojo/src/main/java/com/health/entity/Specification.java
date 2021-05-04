package com.health.entity;

import com.health.pojo.TbSpecification;
import com.health.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {
    private TbSpecification specification;

    private List<TbSpecificationOption> specificationOptionList;

    public Specification() {
        super();
    }

    public Specification(TbSpecification specification, List<TbSpecificationOption> specificationOptionList) {
        super();
        this.specification = specification;
        this.specificationOptionList = specificationOptionList;
    }

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
