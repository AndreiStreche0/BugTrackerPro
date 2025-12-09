package processing;

public class Comment {
    private String author;
    private String content;
    private String createdAt;

    public Comment(String author, String content, String createdAt) {
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setaAthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
