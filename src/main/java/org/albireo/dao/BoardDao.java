/*
 * Copyright Cboard
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.albireo.dao;

import org.albireo.pojo.DashboardBoard;
import org.albireo.pojo.DashboardBoardParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/23.
 */
@Component
public interface BoardDao {

    int save(DashboardBoard board);

    List<DashboardBoard> getBoardList(String userId);

    List<DashboardBoard> getBoardListAdmin(String userId);

    long countExistBoardName(Map<String, Object> map);

    int update(DashboardBoard board);

    int delete(Long id, String userId);

    DashboardBoard getBoard(Long id);

    long checkBoardRole(String userId, Long boardId, String permissionPattern);

    DashboardBoardParam getBoardParam(Long boardId, String userId);

    int saveBoardParam(DashboardBoardParam boardParam);

    int deleteBoardParam(Long boardId, String userId);
}
