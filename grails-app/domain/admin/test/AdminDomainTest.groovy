package admin.test

class AdminDomainTest{

    String name
    Integer age
    String email

    static constraints = {
        age min:18
        email email:true
    }



}
