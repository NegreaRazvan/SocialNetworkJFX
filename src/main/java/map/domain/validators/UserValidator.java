package map.domain.validators;


import map.domain.User;

public class UserValidator implements Validator<User> {

    /**
     * checks whether the first and the last name are valid
     * @param entity - a user who had a fist and a last name
     * @throws ValidationException "Utilizatorul nu este valid" - if the first or the last name are not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getFirstName().equals(""))
            throw new ValidationException("Utilizatorul nu este valid");
        if(entity.getLastName().equals(""))
            throw new ValidationException("Utilizatorul nu este valid");
    }
}
