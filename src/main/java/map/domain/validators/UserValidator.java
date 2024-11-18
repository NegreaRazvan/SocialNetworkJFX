package map.domain.validators;


import map.domain.User;

import java.util.function.Predicate;

public class UserValidator implements Validator<User> {

    private int howManyCharacters(String string, Predicate<Character> predicate) {
        return (int)string.chars()
                .mapToObj(c -> (char) c)
                .filter(predicate)
                .count();
    }

    private String isPasswordValid(String password){
        String errors="";
        if(howManyCharacters(password, Character::isDigit)==0)
            errors+="Password should have at least one digit\n";
        if(howManyCharacters(password, Character::isUpperCase)==0)
            errors+="Password should have at least one uppercase letter\n";
        if(howManyCharacters(password, Character::isLowerCase)==0)
            errors+="Password should have at least one lowercase letter\n";
        Predicate<Character> specialCharacter = Character::isDigit;
        specialCharacter = specialCharacter.or(Character::isLetter).or(Character::isWhitespace);

        if(howManyCharacters(password, specialCharacter)==password.length())
            errors+="Password should have at least one special character\n";
        if(password.length()<5)
            errors+="Password should have at least 5 characters\n";
        return errors;
    }

    /**
     * checks whether the first and the last name are valid
     * @param entity - a user who had a fist and a last name
     * @throws ValidationException "Utilizatorul nu este valid" - if the first or the last name are not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String errors="";
        if(entity.getFirstName().equals(""))
            errors+="First name cannot be empty\n";
        if(entity.getLastName().equals(""))
            errors+="Last name cannot be empty\n";
        if(!isPasswordValid(entity.getPassword()).isEmpty())
            errors+=isPasswordValid(entity.getPassword());
        if(!errors.isEmpty())
            throw new ValidationException(errors);
        if(howManyCharacters(entity.getUsername(), Character::isUpperCase)!=0)
            throw new UsernameUpperCaseException(entity.getUsername().toLowerCase());

    }
}
