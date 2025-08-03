// Компонент для графика работы (табель)
function TimesheetTable({ attendance, users, shifts, onSaveAttendance, onDeleteAttendance }) {
    const [selectedDate, setSelectedDate] = React.useState(new Date().toISOString().split('T')[0]);
    const [attendanceData, setAttendanceData] = React.useState([]);
    const [loading, setLoading] = React.useState(false);

    // Загрузка данных посещаемости
    React.useEffect(() => {
        if (selectedDate) {
            loadAttendanceData();
        }
    }, [selectedDate]);

    const loadAttendanceData = async () => {
        setLoading(true);
        try {
            const response = await fetch(`/api/attendance/date/${selectedDate}`);
            if (response.ok) {
                const data = await response.json();
                setAttendanceData(data);
            }
        } catch (error) {
            console.error('Ошибка загрузки данных посещаемости:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleDateChange = (e) => {
        setSelectedDate(e.target.value);
    };

    const handleAttendanceChange = (userId, field, value) => {
        setAttendanceData(prev => 
            prev.map(item => 
                item.userId === userId 
                    ? { ...item, [field]: value }
                    : item
            )
        );
    };

    const handleSave = async () => {
        try {
            for (const item of attendanceData) {
                if (item.id) {
                    await fetch(`/api/attendance/${item.id}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(item)
                    });
                } else {
                    await fetch('/api/attendance', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(item)
                    });
                }
            }
            loadAttendanceData();
        } catch (error) {
            console.error('Ошибка сохранения:', error);
        }
    };

    return (
        <div className="timesheet-container">
            <div className="timesheet-header">
                <h2>График работы</h2>
                <div className="date-selector">
                    <label>Дата: </label>
                    <input 
                        type="date" 
                        value={selectedDate} 
                        onChange={handleDateChange}
                        className="form-control"
                    />
                </div>
                <button 
                    className="btn btn-primary" 
                    onClick={handleSave}
                    disabled={loading}
                >
                    {loading ? 'Сохранение...' : 'Сохранить'}
                </button>
            </div>

            <div className="timesheet-table">
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <th>Сотрудник</th>
                            <th>Смена</th>
                            <th>Отсутствует</th>
                            <th>Примечания</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => {
                            const userAttendance = attendanceData.find(a => a.userId === user.id) || {
                                userId: user.id,
                                userName: user.name,
                                shiftId: null,
                                isAbsent: false,
                                notes: ''
                            };

                            return (
                                <tr key={user.id}>
                                    <td>{user.name}</td>
                                    <td>
                                        <select 
                                            className="form-control"
                                            value={userAttendance.shiftId || ''}
                                            onChange={(e) => handleAttendanceChange(user.id, 'shiftId', e.target.value ? parseInt(e.target.value) : null)}
                                        >
                                            <option value="">Без смены</option>
                                            {shifts.map(shift => (
                                                <option key={shift.id} value={shift.id}>
                                                    {shift.name} ({shift.startTime} - {shift.endTime})
                                                </option>
                                            ))}
                                        </select>
                                    </td>
                                    <td>
                                        <input 
                                            type="checkbox" 
                                            checked={userAttendance.isAbsent || false}
                                            onChange={(e) => handleAttendanceChange(user.id, 'isAbsent', e.target.checked)}
                                        />
                                    </td>
                                    <td>
                                        <input 
                                            type="text" 
                                            className="form-control"
                                            value={userAttendance.notes || ''}
                                            onChange={(e) => handleAttendanceChange(user.id, 'notes', e.target.value)}
                                            placeholder="Примечания"
                                        />
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

// Экспорт для использования в основном файле
if (typeof module !== 'undefined' && module.exports) {
    module.exports = TimesheetTable;
} 