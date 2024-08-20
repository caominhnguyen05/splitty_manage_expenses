package server.api;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TagRepository;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    //TODO: getAllTags, getSpecificTag, updateTag, removeTag, createNew

    private final TagRepository repo;

    /**
     * Initializes a new Tag controller endpoint.
     *
     * @param repo Tag repository that connects to the database.
     */
    public TagController(TagRepository repo) {
        this.repo = repo;
    }

    /**
     * Returns Tag with given id.
     *
     * @param id id of the Tag
     * @return response with Tag with given id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Deletes Tag with given id.
     *
     * @param id id of the Tag
     * @return http response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Updates Tag with given id.
     *
     * @param id    id of the Tag
     * @param tag updated Tag
     * @return updated Tag with given id.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateById(@PathVariable("id") long id, @RequestBody Tag tag) {
        if (id < 0 || !repo.existsById(id) || tag.getName() == null || tag.getColor() == null) {
            return ResponseEntity.badRequest().build();
        }

        Tag oldTag = repo.findById(id).get();

        oldTag.setColor(tag.getColor());
        oldTag.setName(tag.getName());

        Tag saved = repo.save(oldTag);

        return ResponseEntity.ok(saved);
    }

    /**
     * Creates new Tag.
     *
     * @param tag Tag to be added
     * @return given Tag.
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        System.out.println("Adding new tag: " + tag.toString());
        if (tag.getName() == null || tag.getColor() == null) {
            return ResponseEntity.badRequest().build();
        }

        Tag saved = repo.save(tag);
        return ResponseEntity.ok(saved);
    }

    /**
     * Returns all Tag in the database
     *
     * @return response with Tags
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Tag>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }
}
