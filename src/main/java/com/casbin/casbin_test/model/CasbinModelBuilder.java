package com.casbin.casbin_test.model;

import org.casbin.jcasbin.model.Model;

public class CasbinModelBuilder {

    public static Model buildModel() {
        Model model = new Model();
        model.addDef("r", "r", "sub, obj, act");       // Request
        model.addDef("p", "p", "sub, obj, act");       // Policy
        model.addDef("e", "e", "some(where (p.eft == allow))"); // Effect
        model.addDef("m", "m", "r.sub == p.sub && r.obj == p.obj && r.act == p.act"); // Matchers
        return model;
    }
}
