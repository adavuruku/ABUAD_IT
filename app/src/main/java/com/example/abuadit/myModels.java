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

    public class companyModel {

        private String description;
        private String ContactAdd;
        private String State;
        private String lgov;
        private String PhoneNo;
        private String Email;
        private String companyName;
        private String companyId;
        private String appStatus;

        public companyModel(String description, String contactAdd, String state, String lgov, String phoneNo, String email, String companyName, String companyId, String appStatus) {
            this.description = description;
            ContactAdd = contactAdd;
            State = state;
            this.lgov = lgov;
            PhoneNo = phoneNo;
            Email = email;
            this.companyId = companyId;
            this.companyName = companyName;
            this.appStatus = appStatus;
        }

        public String getDescription() {
            return description;
        }
        public String getCompanyId() {
            return companyId;
        }

        public String getContactAdd() {
            return ContactAdd;
        }

        public String getState() {
            return State;
        }

        public String getLgov() {
            return lgov;
        }

        public String getAppStatus() {
            return appStatus;
        }

        public String getPhoneNo() {
            return PhoneNo;
        }

        public String getEmail() {
            return Email;
        }

        public String getCompanyName() {
            return companyName;
        }
    }
    public class notice {

        private String body;
        private String title;
        private String author;
        private String date;
        private String noticeId;

        public notice(String body, String title, String author, String date, String noticeId) {
            this.body = body;
            this.title = title;
            this.author = author;
            this.date = date;
            this.noticeId = noticeId;
        }

        public String getBody() {
            return body;
        }

        public String getNoticeID() {
            return noticeId;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getDate() {
            return date;
        }
    }
}
