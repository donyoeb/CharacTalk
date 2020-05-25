package com.google.wjddidgns22;

public class Blog_item {
        String poster; //사진
        String title;    //제목
        String day;   //날짜
        public Blog_item(String pp,String tt,String dd){
            this.poster = pp ;
            this.title=tt;
            this.day=  dd;
        }

        public String getPoster() {
            return this.poster; }

        public String getTitle() {
            return this.title;
        }

        public String getDay() {
            return this.day;
        }

         public void setPoster(String pp) {
             poster = pp;

         }
         public void setTitle(String tt) {
            title = tt;
    }

          public void setDay(String dd) {
            day = dd;
    }


    }

