package com.google.wjddidgns22;

public class Blog_item {
        String poster; //사진
        String title;    //제목
        String day;   //날짜

        public Blog_item(String pp,String tt,String dd){
            this.poster = pp;
            this.title=tt;
            this.day=dd;
        }

        public String getPoster() { return poster; }

        public String getTitle() {
            return title;
        }

        public String getDay() {
            return day;
        }



    }

