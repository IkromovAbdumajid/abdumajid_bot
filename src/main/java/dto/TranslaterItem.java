package dto;

import enums.TodoItemType;
import enums.TranslaterType;

import java.util.Date;

public class TranslaterItem {


        private String id;

        private String title;

        private Date createdDate;

        private Long userId;
    private TranslaterType type;

    public TranslaterItem() {
    }

    public String getId() {

            return id;

        }


        public void setId(String id) {

            this.id = id;

        }


        public String getTitle() {

            return title;

        }


        public void setTitle(String title) {

            this.title = title;

        }




        public Date getCreatedDate() {

            return createdDate;

        }


        public void setCreatedDate(Date createdDate) {

            this.createdDate = createdDate;

        }


        public Long getUserId() {

            return userId;

        }


        public void setUserId(Long userId) {

            this.userId = userId;

        }

    public TranslaterType getType() {
        return type;
    }

    public void setType(TranslaterType type) {
        this.type = type;
    }
}