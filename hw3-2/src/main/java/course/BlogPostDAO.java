package course;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {

        // TODO XXX HW 3.2,  Work Here
        Bson filter = Filters.eq("permalink", permalink);

        Document post = postsCollection.find()
            .filter(filter)
            .first();

        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // TODO XXX HW 3.2,  Work Here
        Bson sort = Sorts.orderBy(Sorts.descending("date"));

        List<Document> posts = postsCollection.find()
            .sort(sort)
            .limit(limit)
            .into(new ArrayList<Document>());

        return posts;
    }


    public String addPost(String title, String body, List<String> tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        // TODO XXX HW 3.2, Work Here
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        Document post = new Document();

        post.append("title", title);
        post.append("author", username);
        post.append("body", body);
        post.append("permalink", permalink);
        post.append("tags", tags);
        post.append("comments", new ArrayList<String>());
        post.append("date", new Date());

        try {
            postsCollection.insertOne(post);
        }
        catch (MongoWriteException e) {
            System.out.println("Error occurs while adding post : " + e.getMessage());
            throw e;
        }

        return permalink;
    }




    // White space to protect the innocent








    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        // TODO XXX HW 3.3, Work Here
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments

        Document post = this.findByPermalink(permalink);

        Document comment = new Document();
        comment.append("author", name);
        comment.append("body", body);
        if(StringUtils.isNotBlank(email)) {
            comment.append("email", email);
        }

        Document update = new Document();
        update.put("$push", new Document("comments", comment));
        postsCollection.updateOne(post, update);

        // CLO
        //Bson filter = Filters.eq("permalink", permalink);
        //postsCollection.updateOne(filter, update);

    }

}
