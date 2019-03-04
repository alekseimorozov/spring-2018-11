$(function () {
    $.get('/api/all', function (tasks) {
        tasks.forEach(function (task) {
            $('tbody').append(`
                    <tr id=${task.id}>
                    <td>${task.title}</td>
                    <td>${task.description}</td>
                    <td>${task.status}</td>
                    <td>${task.progress}</td>
                    <td>
                        <a href="edit?taskId=${task.id}" class="btn btn-primary btn-sm">edit</a>
                    </td>
                    <td>
                    <a class="btn btn-danger btn-sm" onclick=deleteTask("${task.id}")>DELETE</a>
                    </td>
                </tr>
                `)
        });
    })
});

function deleteTask(id) {
    $.ajax({
        url: "/api/delete?taskId=" + id,
        type: "DELETE",
        success: function(result){
            $("tr").remove("#" + id);
        }
    });
}