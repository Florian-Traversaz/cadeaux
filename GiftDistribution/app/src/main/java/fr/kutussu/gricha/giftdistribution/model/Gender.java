package fr.kutussu.gricha.giftdistribution.model;

/**
 * Created by flori on 20/11/2016.
 */

public enum Gender {
    MALE,
    FEMALE;

    public static boolean getBooleanFromGender(Gender gender){
        if(gender.equals(Gender.MALE)){
            return true;
        }
        else{
            return false;
        }
    }

    public static Gender getGenderFromBoolean(Boolean gender){
        if(gender){
            return Gender.MALE;
        }
        else{
            return Gender.FEMALE;
        }
    }
}
