package admin.test

class TestConfigDomain {
    String name
    Integer year

    static constraints = {
        name nullable:false
        year nullable:true
    }
}
