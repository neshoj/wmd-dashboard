{
    FormValidation.Validator.password = utils.passwordValidator;
    $("form").formValidation({
        framework: 'bootstrap',
        fields: {
            email: {
                verbose: false,
                validators: {
                    notEmpty: {message: "Email is required"},
                    emailAddress: {message: 'The email address is invalid'},
                    stringLength: {max: 512, message: 'Cannot exceed 512 characters'}
                }
            },
            password: {
                validators: {
                    notEmpty: {message: "Password is required"},
                    password: {message: "Invalid password"},
                    stringLength: {max: 14, message: 'Cannot exceed 14 characters'}
                }
            }

        }
    });
}