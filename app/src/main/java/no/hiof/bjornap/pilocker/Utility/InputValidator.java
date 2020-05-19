package no.hiof.bjornap.pilocker.Utility;

import android.util.Pair;

public class InputValidator {


    public static Pair<Boolean, String> isDoorNameGood(String name){
        //  Rules for door name:
        //      * Name is not only spaces
        //      * Minimum 2 characters
        //      * Maximum 15 characters
        //      * Only containing numbers and letters
        //  Usage:
        //      Pair<Boolean, String> result = InputValidator.isDoorNameGood(<StringWithDoorName>);
        //      result.getKey(); <-- Gets true/false
        //      result.getValue(); <-- Gets the message


        //Removing all spaces in case the name consists of just spaces
        String removedSpaces = name.replaceAll("\\s+", "");
        //If it only contains spaces, then return false
        if(removedSpaces.isEmpty()){
            return new Pair<>(false, "The name is empty");
        }

        //Checking if the length is between 2 and 15
        int nameLength = name.length();
        if(nameLength < 2 || nameLength > 15){
            return new Pair<>(false, "The name needs to be between 2 and 15 characters");
        }

        //If the name contains sometghing else than letters and numbers
        if(!onlyLettersAndNumbersAndSomeSpecials(name)){
            return new Pair<>(false, "Some character is not allowed");
        }

        return new Pair<>(true, "All good");
    }


    public static Pair<Boolean, String> isPasswordGood(String password){

        //  Rules for password:
        //      * Minimum 10 characters
        //      * Containing at least:
        //          * 1 small character
        //          * 1 big character
        //          * 1 number
        //          * 1 special character
        //  Usage:
        //      Pair<Boolean, String> result = InputValidator.isPasswordGood(<StringWithPassword>);
        //      result.getKey(); <-- Gets true/false
        //      result.getValue(); <-- Gets the message

        //Checking the password length
        int passwordLength = password.length();
        if(passwordLength < 10){
            return new Pair<>(false, "The password is to short");
        }

        //Checking for containing
        if(!password.matches("^(?=.*[a-zæøå])(?=.*[A-ZÆØÅ])(?=.*\\d)(?=.*[@$!%*?&])[A-ZÆØÅa-zæøå\\d@$!%<>\\\\\\/*?& ,\\-.:_;|]{10,}$")){
            return new Pair<>(false, "Password needs to at least have: 1 small character, 1 big character, 1 number and 1 special character");
        }

        return new Pair<>(true, "All good");
    }

    public static Pair<Boolean, String> isServiceModePasswordGood(String password, String repeat){

        if (!password.equals(repeat)) {
            return new Pair<>(false, "Password does not match");
        }

        int length = password.length();
        if (length > 6){
            return new Pair<>(false, "Minimum 6 characters");
        }

        if(!password.matches("[A-ZÆØÅa-zæøå0-9]")){
            return new Pair<>(false, "Only numbers and digits allowed");
        }

        return new Pair<>(true, "All good");
    }

    public static Pair<Boolean, String> isPinGood(String pin, String repeat){


        if (!pin.matches("\\d+") || !repeat.matches("\\d+")){
            return new Pair<>(false, "only numbers 0-9 allowed");
        }

        if (pin.length() < 4 || repeat.length() < 4 ){
            return new Pair<>(false, "minimum four digits");
        }

        if(pin.length() > 9 || repeat.length() > 9){
            return new Pair<>(false, "maximum twelve digits");
        }

        if (!pin.equals(repeat)){
            return new Pair<>(false, "PIN codes does not match");
        }

        return new Pair<>(true, "All good");
    }


    public static Pair<Boolean, String> isMailGood(String mail){

        //  Rules for mail:
        //      * Minimum 2 and max 35 characters before @
        //      * Ending with "@gmail.com"
        //  Usage:
        //      Pair<Boolean, String> result = InputValidator.isMailGood(<StringWithMail>);
        //      result.getKey(); <-- Gets true/false
        //      result.getValue(); <-- Gets the message

        if (!mail.contains("@")){
            return new Pair<>(false, "not in the correct format");
        }

        //Separating what's before and after @
        String[] mailParts = mail.split("@");
        //Adding @ to start of the last piece
        mailParts[1] = "@"+mailParts[1];

        //If there are more than on @, then false
        if(mailParts.length != 2){
            return new Pair<>(false, "Something was wrong with the mail address");
        }

        //If the first part dosn't match the length constraint
        if(mailParts[0].length() < 2 || mailParts[0].length() > 35){
            return new Pair<>(false, "Your mail needs to be between 2 and 35 characters");
        }

        //If the first part is not only letters and numbers
        if(!onlyLettersAndNumbersAndSomeSpecials(mailParts[0])){
            return new Pair<>(false, "The first part of your mail can only contain letters and numbers");
        }

        //The last part has to be "@gmail.com"
        if(!mailParts[1].equals("@gmail.com")){
            return new Pair<>(false, "The mail needs to be a gmail. It has to end on \"@gmail.com\"");
        }

        return new Pair<>(true, "All good");
    }

    //Method for checking of the string only contains letters and numbers
    private static boolean onlyLettersAndNumbersAndSomeSpecials(String value){
        return value.matches("[a-zæøåA-ZÆØÅ0-9 .\\-_+]*");
    }
}
