package com.cure.common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cure.common.toolkit.ObjectUtil;
import com.cure.common.toolkit.ResultUtil;
import com.cure.common.vo.PageVo;
import com.cure.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: dengmiao
 * @create: 2019-04-09 17:46
 **/
@Slf4j
@RestController
public abstract class BaseController<E, ID extends Serializable> {

    private final IBaseJpaService<E,ID> jpaService;

    private final IBaseMyBatisService<E> myBatisService;

    public BaseController(IBaseJpaService<E,ID> jpaService, IBaseMyBatisService<E> myBatisService) {
        this.jpaService = jpaService;
        this.myBatisService = myBatisService;
    }

    public ResponseEntity<Result> getResult(Result result, HttpStatus status) {
        return new ResponseEntity<>(result, status);
    }

    /**
     * 基于mybatis的分页
     * @param pageVo
     * @param e
     * @return
     */
    @GetMapping("list")
    public Result<?> getByPage(@ModelAttribute PageVo pageVo, E e){
        QueryWrapper<E> queryWrapper = new QueryWrapper<>(e);
        Page<E> page = new Page<>(pageVo.getPageNo(), pageVo.getPageSize());
        //排序逻辑 处理
        String column = pageVo.getColumn(), order = pageVo.getOrder();
        if(ObjectUtil.isNotEmpty(column) && ObjectUtil.isNotEmpty(order)) {
            if("asc".equals(order)) {
                queryWrapper.orderByAsc(ObjectUtil.camelToUnderline(column));
            }else {
                queryWrapper.orderByDesc(ObjectUtil.camelToUnderline(column));
            }
        }
        IPage<E> pageList = myBatisService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 单表添加
     * @param jsonObject
     * @return
     */
    @PostMapping("add")
    public Result<E> add(@RequestBody JSONObject jsonObject) {
        Result<E> result = new Result<>();
        try {
            Class<E> clazz = (Class <E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            E e = JSON.parseObject(jsonObject.toJSONString(), clazz);
            myBatisService.save(e);
            result.success("操作成功");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 单表修改
     * @param jsonObject
     * @return
     */
    @PutMapping("edit")
    public Result<E> edit(@RequestBody JSONObject jsonObject) {
        Result<E> result = new Result<>();
        Class<E> clazz = (Class <E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        E e = JSON.parseObject(jsonObject.toJSONString(), clazz);
        boolean ok = myBatisService.updateById(e);
        if(ok) {
            result.success("操作成功");
        } else {
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 单表单删除
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    public Result<E> delete(@RequestParam(name="id") String id) {
        Result<E> result = new Result<>();
        boolean ok = myBatisService.removeById(id);
        if(ok) {
            result.success("操作成功");
        } else {
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 单表批量删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @Transactional(rollbackFor = Exception.class)
    public Result<E> deleteBatch(@RequestParam(name="ids") String ids) {
        Result<E> result = new Result<>();
        if(ids==null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            boolean ok = myBatisService.removeByIds(Arrays.asList(ids.split(",")));
            if(ok) {
                result.success("操作成功");
            } else {
                result.error500("操作失败");
            }
        }
        return result;
    }

    /******************************************************************************************************************/

    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Result<E> get(@PathVariable ID id){
        E entity = jpaService.get(id);
        return new ResultUtil<E>().setData(entity);
    }

    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<E>> getAll(){

        List<E> list = jpaService.getAll();
        return new ResultUtil<List<E>>().setData(list);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public Result<E> save(@ModelAttribute E entity){

        E e = jpaService.saveEntity(entity);
        return new ResultUtil<E>().setData(e);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @ResponseBody
    public Result<E> update(@ModelAttribute E entity){

        E e = jpaService.update(entity);
        return new ResultUtil<E>().setData(e);
    }

    @RequestMapping(value = "/delByIds/{ids}",method = RequestMethod.DELETE)
    @ResponseBody
    public Result<Object> delAllByIds(@PathVariable ID[] ids){

        for(ID id:ids){
            jpaService.delete(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }
}
