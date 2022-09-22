package Classes;


import Interfaces.ITopic;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AP57630
 */
enum Format{
    XML, JSON;
}
public class Topic implements ITopic{
    private String name;
    private List pub, sub;

    @Override
    public String getName() {
        return name;
    }


    @Override
    public List getPub() {
        return pub;
    }

    @Override
    public List getSub() {
        return sub;
    }
    
    
    
}
