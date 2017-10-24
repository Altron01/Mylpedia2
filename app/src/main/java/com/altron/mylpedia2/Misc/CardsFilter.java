package com.altron.mylpedia2.Misc;

import com.altron.mylpedia2.Misc.Custom_Classes.Card;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class Filtrator {

    public ArrayList<Card> Filter_List;
    public ArrayList<Card> UnFilter_List;
    public void Odenador(List<Card> Card_List){
        Filter_List = new ArrayList<>(Card_List);
        UnFilter_List = new ArrayList<>(Card_List);

    }
    public List<Card> Filter(String name, String race, int cost, int strength, String _class, String edition){
        if(name.equals("") && race.equals("-------Todas-------") && cost == -2 && strength == -2 && _class.equals("-------Todas-------") && edition.equals("-------Todas-------"))return Filter_List;
        Iterator<Card> it = Filter_List.iterator();
        while(it.hasNext()){
            Card card = it.next();

            String name_param = name.toLowerCase();
            String name_card = card.getName().toLowerCase();
            if(!(name.equals(""))) {
                if (!(name_card.contains(name_param))) {
                    it.remove();
                    continue;
                }
            }
            if(!(race.equals("-------Todas-------"))){
                if(!(card.getRace().equals(race))){
                    it.remove();
                    continue;
                }
            }
            if(!(cost == -1)){
                if(!(card.getCost() == cost)){
                    it.remove();
                    continue;
                }
            }
            if(!(strength == -1)){
                if(!(card.getStrength() == strength)){
                    it.remove();
                    continue;
                }
            }
            if(!(_class.equals("-------Todas-------"))){
                if(!(card.get_sort().equals(_class))){
                    it.remove();
                    continue;
                }
            }
            if(!(edition.equals("-------Todas-------"))){
                if(!(card.getEdition().equals(edition))){
                    it.remove();
                }
            }
        }
        return Filter_List;
    }
    public List<Card> Restart_List(){
        Filter_List.clear();
        Filter_List = new ArrayList<>(UnFilter_List);
        return Filter_List;
    }

}