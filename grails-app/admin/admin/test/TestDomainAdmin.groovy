package admin.test

class TestDomainAdmin {
    static options = {
        list excludes: ['name', 'year']
        create excludes: ['name', 'year']
        edit excludes: ['name', 'year']
    }
}
