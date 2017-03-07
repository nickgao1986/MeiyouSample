package nickgao.com.meiyousample.utils.model;


import java.io.Serializable;

/**
 * 
  表情对象实体
 */
public class EmojiModel  implements Serializable{

    /** 表情资源图片对应的ID */
    private int id;

    /** 表情资源对应的文字描述 */
    private String character;

    /** 表情资源的文件名 */
    private String emojiName;

    /** 表情资源图片对应的ID */
    public int getId() {
        return id;
    }

    /** 表情资源图片对应的ID */
    public void setId(int id) {
        this.id=id;
    }

    /** 表情资源对应的文字描述 */
    public String getCharacter() {
        return character;
    }

    /** 表情资源对应的文字描述 */
    public void setCharacter(String character) {
        this.character=character;
    }

    /** 表情资源的文件名 */
    public String getEmojiName() {
        return emojiName;
    }

    /** 表情资源的文件名 */
    public void setEmojiName(String emojiName) {
        this.emojiName = emojiName;
    }
}
