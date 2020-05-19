package com.google.wjddidgns22;

public class Blog_item {
        int poster; //사진
        String title;    //제목
        String day;   //날짜
        public Blog_item(int pp,String tt,String dd){
            this.poster = pp;
            this.title=tt;
            this.day=dd;
        }
         public int getPoster() {
            return poster;
         }

        public void setPoster(int poster) {
            this.poster = poster;
         }
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }


    }

