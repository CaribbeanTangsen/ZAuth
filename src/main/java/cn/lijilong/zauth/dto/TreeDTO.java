package cn.lijilong.zauth.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("树形结构")
@AllArgsConstructor
@NoArgsConstructor
public class TreeDTO {

    private String label;
    private String value;
    private boolean disable;
    private List<TreeDTO> children;

    public static  <T> List<TreeDTO> treeRecursive(T id, List<? extends TreeEntity<?,T>> treeEntity) {
        List<TreeDTO> treeDTOS = new ArrayList<>();

        for (TreeEntity<?, T> tTreeEntity : treeEntity) {
            if (id.equals(tTreeEntity.getSuperId())) {
                treeDTOS.add(new TreeDTO(tTreeEntity.getLabel(), String.valueOf(tTreeEntity.getValue()), tTreeEntity.isDisable(), treeRecursive(tTreeEntity.getValue(), treeEntity)));
            }
        }
        return treeDTOS;
    }

}
