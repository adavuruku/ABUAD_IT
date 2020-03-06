package com.example.abuadit;

public class myModels {

    public class itfOffice {

        private String AreaOffice;
        private String ContactAdd;
        private String State;
        private String PhoneNo;
        private String Email;

        public itfOffice(String areaOffice, String contactAdd, String state, String phoneNo, String email) {
            AreaOffice = areaOffice;
            ContactAdd = contactAdd;
            State = state;
            PhoneNo = phoneNo;
            Email = email;
        }

        public String getAreaOffice() {
            return AreaOffice;
        }

        public String getContactAdd() {
            return ContactAdd;
        }

        public String getState() {
            return State;
        }

        public String getPhoneNo() {
            return PhoneNo;
        }

        public String getEmail() {
            return Email;
        }
    }
}
