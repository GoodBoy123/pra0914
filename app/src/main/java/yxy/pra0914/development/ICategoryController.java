package yxy.pra0914.development;


import java.util.List;

import yxy.pra0914.dto.DevelopmentDto;

/**
 * Created by dasu on 2017/4/20.
 *
 * 这不是一个回调接口，而是一个规范接口，定义每个CategoryFragment必须要实现的工作
 */

interface ICategoryController {

    String getCategoryType();

    void updateGanHuo(List<DevelopmentDto> data);

    void onLoadFailed();

}
