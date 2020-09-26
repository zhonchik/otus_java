package ru.otus.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Indices({
        @Index(value = "chatId", type = IndexType.NonUnique),
        @Index(value = "link", type = IndexType.NonUnique),
})
public class Message implements Serializable {
    @Id
    private Long id;
    private long chatId;
    private String link;

    public static Message NewMessage(long chatId, String link) {
        return new Message(NitriteId.newId().getIdValue(), chatId, link);
    }
}
